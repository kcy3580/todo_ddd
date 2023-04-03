package com.ap.todo.domain.valueobjects;

import com.ap.todo.constant.Importance;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Objects;

@Getter
@Slf4j
@ToString
@Builder
public class Priority implements Comparable<Priority> {

    // 중요도
    private Importance importance;

    // 순서
    private int sequence;

    public Priority(Importance importance, int sequence) {
        this.importance = importance;
        this.sequence = sequence;
    }

    @Override
    public int compareTo(Priority o) {
        // 두 중요도가 같다면 순서에 따라 정렬된다.
        if(Objects.equals(o.getImportance().getImportanceSeq(), this.importance.getImportanceSeq())) {
            return this.sequence - o.getSequence();
        } else {
            return o.getImportance().getImportanceSeq() - this.importance.getImportanceSeq();
        }
    }

}
