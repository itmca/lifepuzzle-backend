package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import io.itmca.lifepuzzle.global.util.FileUtil;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroWriteRequest {

    private Long heroNo;
    private String heroName;
    private String heroNickName;
    private LocalDate birthday;
    private String title;
    private String imageURL;

    public Hero toHero(){
        return toHeroOf(heroNo);
    }

    public Hero toHeroOf(Long heroNo){
        return toHeroOf(heroNo, null);
    }

    public Hero toHeroOf(MultipartFile photo){
        return toHeroOf(heroNo, photo);
    }

    public Hero toHeroOf(Long heroNo, MultipartFile photo){
        var imageURL = removeFileServerURLInImage();

        if (FileUtil.isMultiPartFile(photo)) {
            imageURL = FileUtil.addRandomValueFilePrefix(photo.getOriginalFilename());
        }

        return Hero.builder()
                .heroNo(heroNo)
                .name(heroName)
                .nickname(heroNickName)
                .birthday(birthday)
                .title(title)
                .image(imageURL)
                .build();
    }

    private String removeFileServerURLInImage(){
        var fileServerURL = String.format("%s/hero/profile/%d/", ServerConstant.SERVER_HOST, heroNo);
        return imageURL.replace(fileServerURL, "");
    }
}
