package com.prithak.task_organizer.model;

import com.prithak.task_organizer.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task extends AuditUser {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_sequence")
  @SequenceGenerator(name = "task_sequence", sequenceName = "task_seq", allocationSize = 1)
  private Long id;
  @Column(nullable = false)
  @NotNull
  @Size(min = 1)
  private String title;
  private String description;
  @Column(name = "due_date")
  private LocalDate dueDate;
  private String attachment;
  @NotNull
  @ManyToOne
  @JoinColumn(name = "assignee_id", referencedColumnName = "id")
  private User assignee;
  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;
  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
  private boolean deleted;

  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setTask(this);
  }

  public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setTask(null);
  }
}
