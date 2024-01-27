package com.grocery.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Paging {
    long totalPages;
    boolean hasNext;
    Next next;

    @Setter
    @Getter
    @Builder
     public static class Next {
        String link;
    }

}
