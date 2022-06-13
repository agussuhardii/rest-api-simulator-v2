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

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

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

  @Column(name = "main_path_url", nullable = false)
  private String mainPathUrl;

  @Column(name = "path_url", nullable = false)
  private String pathUrl;

  @Column(name = "method", nullable = false)
  private String method;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "request_body_or_params", nullable = false)
  private Map<String, Object> requestBodyOrParams;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "success_header", nullable = false)
  private Map<String, Object> successHeader;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "success_body", nullable = false)
  private Map<String, Object> successBody;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "fail_header", nullable = false)
  private Map<String, Object> failHeader;

  @Type(type = "jsonb")
  @Column(columnDefinition = "json", name = "fail_body", nullable = false)
  private Map<String, Object> failBody;

  @Column(name = "created_at")
  private long createdAt;

  @Column(name = "updated_at")
  private long updatedAt;
}
