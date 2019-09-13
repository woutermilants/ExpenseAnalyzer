package be.milants.expenseanalyzer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageRequestUtil {

    public static PageRequest getPageRequest(Integer page, Integer size, String sortColumn, String direction) {
        Sort sort = Sort.unsorted();
        Sort.Direction sortDirection = getSortDirection(direction);
        if (StringUtils.isNotEmpty(sortColumn)) {
            if (Sort.Direction.ASC == sortDirection) {
                sort = Sort.by(Sort.Order.asc(sortColumn));
            } else {
                sort = Sort.by(Sort.Order.desc(sortColumn));
            }
        }
        return PageRequest.of(page, size, sort);
    }

    private static Sort.Direction getSortDirection(String direction) {
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.DESC;
            log.error(e.getMessage(), e);
        }
        return sortDirection;
    }
}
