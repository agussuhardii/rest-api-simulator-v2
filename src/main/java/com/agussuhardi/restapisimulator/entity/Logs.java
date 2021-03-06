package com.agussuhardi.restapisimulator.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Table(name = "logs")
@Entity
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({
  @TypeDef(name = "json", typeClass = JsonStringType.class),
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Logs implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private UUID id;

  @Column(name = "uri", nullable = false)
  private String uri;

  @Column(name = "method", nullable = false)
  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, String> headers = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, String[]> params = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> body = new HashMap<>();

  @Column(name = "created_at")
  private long createdAt;

  @Column(name = "updated_at")
  private long updatedAt;

  private int responseCode;

  @PrePersist
  public void setCreatedAt() {
    this.createdAt = System.currentTimeMillis();
  }

  @PreUpdate
  public void setUpdatedAt() {
    this.updatedAt = System.currentTimeMillis();
  }
}
