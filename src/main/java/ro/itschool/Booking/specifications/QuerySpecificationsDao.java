package ro.itschool.Booking.specifications;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Role;

import java.util.List;

@Getter
@Setter
@Service
public class QuerySpecificationsDao {

    @Autowired
    private EntityManager entityManager;

    private QuerySpecificationsDao() {

    }

    public static Specification<Property> getPropertyByPersonFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, Person> propertyPersonJoin = root.join("firstName");
            return criteriaBuilder.equal(propertyPersonJoin.get("firstName"), firstName);
        };
    }


    public List<Property> getAllPropertyByTypeAndName(PropertyTypeAndNameRequest propertyTypeAndNameRequest) {
        String name = propertyTypeAndNameRequest.getName();
        String type = propertyTypeAndNameRequest.getType();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        Root<Property> root = criteriaQuery.from(Property.class);
        if (name == null) {
            name = " ";
        }
        if (type == null) {
            type = " ";
        }
        Predicate propertyNamePredicate = criteriaBuilder.like(root.get("propertyName"), "%" + name + "%");
        Predicate propertyTypePredicate = criteriaBuilder.like(root.get("propertyType"), "%" + type + "%");
        Predicate andPredicate = criteriaBuilder.and(propertyNamePredicate, propertyTypePredicate);
        criteriaQuery.where(andPredicate);
        TypedQuery<Property> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
    public List<Person> getAllPersonByFirstNameOrMobileNumber(PersonRoleAndFirstNameRequest personRoleAndFirstNameRequest) {
        String name = personRoleAndFirstNameRequest.getName();
        String mobileNumber = personRoleAndFirstNameRequest.getMobileNumber();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        if (name == null) {
            name = " ";
        }
        if (mobileNumber == null) {
            mobileNumber = " ";
        }
        Predicate propertyNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + name + "%");
        Predicate propertyTypePredicate = criteriaBuilder.like(root.get("mobileNumber"), "%" + mobileNumber + "%");
        Predicate andPredicate = criteriaBuilder.or(propertyNamePredicate, propertyTypePredicate);
        criteriaQuery.where(andPredicate);
        TypedQuery<Person> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
