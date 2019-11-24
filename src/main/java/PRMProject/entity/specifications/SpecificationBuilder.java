package PRMProject.entity.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class SpecificationBuilder {
        public static <T> Specification<T> build(List<Specification<T>> specs) {
            Specification<T> spec = null;
            if (specs != null && specs.size() > 0) {
                spec = specs.get(0);
                for (int i = 1; i < specs.size(); i++) {
                    spec = Specification.where(spec).and(specs.get(i));
                }
            }
            return spec;
        }
}
