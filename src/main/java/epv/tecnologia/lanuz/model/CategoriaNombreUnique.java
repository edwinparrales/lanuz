package epv.tecnologia.lanuz.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import epv.tecnologia.lanuz.service.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the nombre value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = CategoriaNombreUnique.CategoriaNombreUniqueValidator.class
)
public @interface CategoriaNombreUnique {

    String message() default "{Exists.categoria.nombre}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CategoriaNombreUniqueValidator implements ConstraintValidator<CategoriaNombreUnique, String> {

        private final CategoriaService categoriaService;
        private final HttpServletRequest request;

        public CategoriaNombreUniqueValidator(final CategoriaService categoriaService,
                final HttpServletRequest request) {
            this.categoriaService = categoriaService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(categoriaService.get(Long.parseLong(currentId)).getNombre())) {
                // value hasn't changed
                return true;
            }
            return !categoriaService.nombreExists(value);
        }

    }

}
