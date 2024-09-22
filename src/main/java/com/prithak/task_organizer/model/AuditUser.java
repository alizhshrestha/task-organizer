package com.prithak.task_organizer.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditUser {
  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
  @LastModifiedDate
  @Column
  private LocalDateTime updatedAt;
  @ManyToOne
  @JoinColumn(name = "created_by")
  private User createdBy;
  @ManyToOne
  @JoinColumn(name = "updated_by")
  private User updatedBy;
}
