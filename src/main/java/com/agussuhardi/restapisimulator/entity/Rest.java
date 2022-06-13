package com.agussuhardi.restapisimulator.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@lombok.Data
@Table(name = "rest")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
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
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "path_url", nullable = false)
  private String pathUrl;

  @Column(name = "method", nullable = false)
  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "request_body", nullable = false)
  private Map<String, Object> requestBody;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "request_params", nullable = false)
  private Map<String, Object> requestParams;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "success_response_header", nullable = false)
  private Map<String, Object> successResponseHeader;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "success_response_body", nullable = false)
  private Map<String, Object> successResponseBody;

  @Column(name = "is_random_success_response_body", nullable = false)
  private boolean randomSuccessResponseBody;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "fail_response_header", nullable = false)
  private Map<String, Object> failResponseHeader;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "fail_response_body", nullable = false)
  private Map<String, Object> failResponseBody;

  @Column(name = "created_at")
  private long createdAt;

  @Column(name = "updated_at")
  private long updatedAt;
}
