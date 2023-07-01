package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "주인공 등록")
public class HeroWriteEndpoint {

  private final HeroValidationService heroValidationService;
  private final HeroWriteService heroWriteService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;
  private final S3UploadService s3UploadService;

  @Operation(summary = "주인공 등록")
  @PostMapping("/heroes")
  public HeroQueryDTO createHero(@RequestPart("toWrite") HeroWriteRequest heroWriteRequest,
                                 @RequestPart(value = "photo", required = false)
                                 MultipartFile reqeustPhoto,
                                 @AuthenticationPrincipal AuthPayload authPayload) {

    var hero = heroWriteService.create(heroWriteRequest.toHeroOf(reqeustPhoto));

    if (reqeustPhoto != null) {
      var heroProfileImage = new HeroProfileImage(hero, reqeustPhoto);

      hero.setImage(heroProfileImage);

      s3UploadService.upload(heroProfileImage);
    }

    heroUserAuthWriteService.create(HeroUserAuth.builder()
        .userNo(authPayload.getUserNo())
        .hero(hero)
        .build());

    return HeroQueryDTO.from(hero);
  }

  @Operation(summary = "주인공 수정")
  @PutMapping("heroes/{heroNo}")
  public HeroQueryDTO updateHero(@RequestBody HeroWriteRequest heroWriteRequest,
                                 @PathVariable("heroNo") Long heroNo,
                                 @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    return HeroQueryDTO.from(heroWriteService.create(heroWriteRequest.toHeroOf(heroNo)));
  }

  @Operation(summary = "주인공 삭제")
  @DeleteMapping("heroes/{heroNo}")
  public void deleteHero(@PathVariable("heroNo") Long heroNo,
                         @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);
    heroWriteService.remove(heroNo);
  }

  @Operation(summary = "주인공 사진 수정")
  @PostMapping("heroes/profile/{heroNo}")
  public HeroQueryDTO saveHeroPhoto(@PathVariable("heroNo") Long heroNo,
                                    @RequestPart("toUpdate") HeroWriteRequest heroWriteRequest,
                                    @RequestPart(name = "photo", required = false)
                                    MultipartFile requestPhoto,
                                    @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    var hero = heroWriteRequest.toHeroOf(heroNo, requestPhoto);

    if (requestPhoto != null) {
      var heroProfileImage = new HeroProfileImage(hero, requestPhoto);

      hero.setImage(heroProfileImage);

      s3UploadService.upload(heroProfileImage);
    }

    return HeroQueryDTO.from(heroWriteService.update(hero));

  }
}
