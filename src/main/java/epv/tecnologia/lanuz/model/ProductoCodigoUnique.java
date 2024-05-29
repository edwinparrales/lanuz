package epv.tecnologia.lanuz.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import epv.tecnologia.lanuz.service.ProductoService;
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
 * Validate that the codigo value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ProductoCodigoUnique.ProductoCodigoUniqueValidator.class
)
public @interface ProductoCodigoUnique {

    String message() default "{Exists.producto.codigo}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ProductoCodigoUniqueValidator implements ConstraintValidator<ProductoCodigoUnique, String> {

        private final ProductoService productoService;
        private final HttpServletRequest request;

        public ProductoCodigoUniqueValidator(final ProductoService productoService,
                final HttpServletRequest request) {
            this.productoService = productoService;
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
            if (currentId != null && value.equalsIgnoreCase(productoService.get(Long.parseLong(currentId)).getCodigo())) {
                // value hasn't changed
                return true;
            }
            return !productoService.codigoExists(value);
        }

    }

}
