package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.global.constant.FileConstant;
import io.itmca.lifepuzzle.global.infra.file.S3Repository;
import io.itmca.lifepuzzle.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HeroWriteService {
    private final HeroRepository heroRepository;
    private final S3Repository s3Repository;

    public Hero create(Hero hero){
        return heroRepository.save(hero);
    }

    public Hero update(Hero hero){
        return heroRepository.save(hero);
    }

    public void remove(Long heroNo){
        heroRepository.deleteById(heroNo);
    };

    public void saveHeroProfile(Hero hero, MultipartFile multipartFile) throws IOException {

        if(!FileUtil.isExistFolder(FileConstant.TEMP_FOLDER_PATH)) {
            FileUtil.createAllFolder(FileConstant.TEMP_FOLDER_PATH);
        }

        var savedFile = FileUtil.saveMultiPartFileInLocal(multipartFile
                ,FileConstant.TEMP_FOLDER_PATH + File.separator + hero.getImage());

        s3Repository.upload(savedFile, String.format("hero/profile/%d/%s", hero.getHeroNo(), hero.getImage()));

        savedFile.delete();
    }
}
