package com.agussuhardi.restapisimulator.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Table(name = "rest")
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
public class Rest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "uri", nullable = false)
  private String uri;

  @Column(name = "method", nullable = false)
  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, String> requestHeaders = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> requestBody = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, String[]> requestParams = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> successResponseHeaders = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> successResponseBody = new HashMap<>();

  @Column(name = "is_random_success_response_body", nullable = false)
  private boolean isRandomSuccessResponseBody;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> failResponseHeaders = new HashMap<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> failResponseBody = new HashMap<>();

  @Column(name = "created_at")
  private long createdAt;

  @Column(name = "updated_at")
  private long updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "success_response_code", nullable = false)
  private HttpStatus successResponseCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "fail_response_code", nullable = false)
  private HttpStatus failResponseCode;

  @Column(name = "response_in_nano_second", nullable = false)
  private Long responseInNanoSecond;

  @PrePersist
  public void setCreatedAt() {
    this.createdAt = System.currentTimeMillis();
  }

  @PreUpdate
  public void setUpdatedAt() {
    this.updatedAt = System.currentTimeMillis();
  }
}
