package altaspring.springrestfulapi.controller;

import altaspring.springrestfulapi.entity.User;
import altaspring.springrestfulapi.model.RegisterQuery;
import altaspring.springrestfulapi.model.RegisterRequest;
import altaspring.springrestfulapi.model.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public Response<String> register(@RequestBody RegisterRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        registerQuery.save(user);
        return Response.<String>builder().data("OK").build();
    }
}
