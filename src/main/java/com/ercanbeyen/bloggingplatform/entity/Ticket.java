package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Ticket {
    private Integer id;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Approval> approvals;

    public Ticket(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        List<Integer> approvalIds = approvals.stream()
                .map(Approval::getId)
                .toList();

        return "Ticket{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", approvalIds=" + approvalIds +
                '}';
    }
}
