package io.itmca.lifepuzzle.domain.story.entity;

import static io.itmca.lifepuzzle.global.constants.FileConstant.ORIGINAL_BASE_PATH;
import static io.itmca.lifepuzzle.global.constants.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import io.itmca.lifepuzzle.domain.story.type.GalleryType;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.jpa.converter.JsonListConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

@Table(name = "story_photo")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Gallery {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long heroId;
  @Column(nullable = false)
  private String url;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AgeGroup ageGroup;
  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private GalleryType galleryType;
  @Lob
  @Convert(converter = JsonListConverter.class)
  private List<Integer> resizedSizes = new ArrayList<>();
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StoryGallery> storyMaps;

  public static List<Gallery> listFrom(List<? extends CustomFile> galleryFiles, Long heroId,
                                       AgeGroup ageGroup, GalleryType galleryType) {
    return galleryFiles.stream().map(storyImageFile ->
        Gallery.builder()
            .heroId(heroId)
            .ageGroup(ageGroup)
            .galleryType(galleryType)
            .url(storyImageFile.getBase() + storyImageFile.getFileName())
            .build()
    ).toList();
  }

  public String getGalleryUrl() {
    if (!StringUtils.hasText(url)) {
      return "";
    }

    return S3_SERVER_HOST + url;
  }

  public String getImageUrl(Integer size) {
    var isExistResizedFile = resizedSizes != null && resizedSizes.contains(size);
    var thumbnailFilePath = isExistResizedFile
        ? url.replace(ORIGINAL_BASE_PATH, String.valueOf(size))
        : url;

    return S3_SERVER_HOST + thumbnailFilePath;
  }

  public void addResizedSizes(List<Integer> resizedSizes) {
    this.resizedSizes.addAll(resizedSizes);
  }

  public boolean isImage() {
    return galleryType == GalleryType.IMAGE;
  }
}
