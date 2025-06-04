package org.barrikeit.chess.core.util.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.barrikeit.chess.core.util.enums.SearchOperation;
import org.barrikeit.chess.core.util.search.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.barrikeit.chess.core.util.TimeUtil.convertLocalDate;
import static org.barrikeit.chess.core.util.TimeUtil.convertLocalDateTime;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class FilterSpecification<T> implements Specification<T> {

  private final SearchCriteria searchCriteria;
  private Map<SearchOperation, BiFunction<Root<T>, CriteriaBuilder, Predicate>>
      operationPredicateMap = new HashMap<>();

  public FilterSpecification(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
    operationPredicateMap.put(SearchOperation.EQUALITY, this::equalityPredicate);
    operationPredicateMap.put(SearchOperation.NEGATION, this::negationPredicate);
    operationPredicateMap.put(SearchOperation.GREATER_THAN, this::greaterThanPredicate);
    operationPredicateMap.put(SearchOperation.LESS_THAN, this::lessThanPredicate);
    operationPredicateMap.put(SearchOperation.LIKE, this::equalityPredicate);
    operationPredicateMap.put(SearchOperation.IS_NULL, this::isNullPredicate);
    operationPredicateMap.put(SearchOperation.IS_NOT_NULL, this::isNotNullPredicate);
  }

  @Override
  public Predicate toPredicate(
      Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (null == this.searchCriteria) {
      return null;
    }
    return operationPredicateMap
        .getOrDefault(searchCriteria.getOperation(), (r, c) -> null)
        .apply(root, criteriaBuilder);
  }

  private Predicate equalityPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (root.get(this.searchCriteria.getKey()).getJavaType() == String.class) {
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get(this.searchCriteria.getKey())),
          "%" + ((String) this.searchCriteria.getValue()).toLowerCase() + "%");
    } else {
      return criteriaBuilder.equal(
          root.get(this.searchCriteria.getKey()), this.searchCriteria.getValue());
    }
  }

  private Predicate negationPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (root.get(this.searchCriteria.getKey()).getJavaType() == String.class) {
      return criteriaBuilder.notLike(
          criteriaBuilder.lower(root.get(this.searchCriteria.getKey())),
          "%" + ((String) this.searchCriteria.getValue()).toLowerCase() + "%");
    } else {
      return criteriaBuilder.notEqual(
          root.get(this.searchCriteria.getKey()), this.searchCriteria.getValue());
    }
  }

  private Predicate greaterThanPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (root.get(this.searchCriteria.getKey()).getJavaType() == LocalDate.class) {
      LocalDate localDate =
          searchCriteria.getValue() instanceof String
              ? convertLocalDate((String) searchCriteria.getValue())
              : (LocalDate) searchCriteria.getValue();
      return criteriaBuilder.greaterThanOrEqualTo(
          root.get(this.searchCriteria.getKey()), localDate);
    } else if (root.get(this.searchCriteria.getKey()).getJavaType() == LocalDateTime.class) {
      LocalDateTime localDateTime =
          searchCriteria.getValue() instanceof String
              ? convertLocalDateTime((String) searchCriteria.getValue())
              : (LocalDateTime) searchCriteria.getValue();
      return criteriaBuilder.greaterThanOrEqualTo(
          root.get(this.searchCriteria.getKey()), localDateTime);
    } else {
      return criteriaBuilder.greaterThanOrEqualTo(
          root.get(this.searchCriteria.getKey()), searchCriteria.getValue().toString());
    }
  }

  private Predicate lessThanPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (root.get(this.searchCriteria.getKey()).getJavaType() == LocalDate.class) {
      LocalDate localDate =
          searchCriteria.getValue() instanceof String
              ? convertLocalDate((String) searchCriteria.getValue())
              : (LocalDate) searchCriteria.getValue();
      return criteriaBuilder.lessThanOrEqualTo(root.get(this.searchCriteria.getKey()), localDate);
    } else if (root.get(this.searchCriteria.getKey()).getJavaType() == LocalDateTime.class) {
      LocalDateTime localDateTime =
          searchCriteria.getValue() instanceof String
              ? convertLocalDateTime((String) searchCriteria.getValue())
              : (LocalDateTime) searchCriteria.getValue();
      return criteriaBuilder.lessThanOrEqualTo(
          root.get(this.searchCriteria.getKey()), localDateTime);
    } else {
      return criteriaBuilder.lessThanOrEqualTo(
          root.get(this.searchCriteria.getKey()), this.searchCriteria.getValue().toString());
    }
  }

  private Predicate isNullPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    return criteriaBuilder.isNull(root.get(this.searchCriteria.getKey()));
  }

  private Predicate isNotNullPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    return criteriaBuilder.isNotNull(root.get(this.searchCriteria.getKey()));
  }
}
