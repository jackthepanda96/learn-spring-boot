package altaspring.springrestfulapi.controller.user;

import altaspring.springrestfulapi.entity.ResponseObject;
import altaspring.springrestfulapi.model.User;
import altaspring.springrestfulapi.model.RegisterQuery;
import altaspring.springrestfulapi.entity.Response;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
@RestController
public class UserController {

    @Autowired
    private RegisterQuery registerQuery;

    @Autowired
    private Validator validator;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response register(@RequestBody RegisterRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.withDefaults().hashToString(12, request.getPassword().toCharArray()));

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        registerQuery.save(user);
        return Response.<String>builder().code(HttpStatus.CREATED.value()).code(HttpStatus.CREATED.value()).message("success register data").build();
    }

    @PostMapping(
            path = "/api/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseObject<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(loginRequest.getPassword());

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(loginRequest);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        User result = registerQuery.findById(loginRequest.getUsername()).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username atau password salah"));

        BCrypt.Result hashCheck = BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(), result.getPassword().toCharArray());

        if (!hashCheck.verified){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username atau password salah");
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(result.getUsername());
        loginResponse.setName(result.getName());

        return  ResponseObject.<LoginResponse>builder().code(HttpStatus.OK.value()).data(loginResponse).build();
    }
}
