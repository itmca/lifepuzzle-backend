package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.repository.HeroWriteRepository;
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
    private final HeroWriteRepository heroWriteRepository;
    private final S3Repository s3Repository;

    public Hero create(Hero hero){
        return heroWriteRepository.save(hero);
    }

    public Hero update(Hero hero){
        return heroWriteRepository.save(hero);
    }

    public void remove(Long heroNo){
        heroWriteRepository.deleteById(heroNo);
    };

    public void saveHeroFile(Hero hero, MultipartFile multipartFile) throws IOException {
        var savedFile = FileUtil.saveMultiPartFileInLocal(multipartFile
                ,FileConstant.TEMP_FOLDER_PATH + File.separator + hero.getImage());

        s3Repository.upload(savedFile, String.format("hero/profile/%d/%s", hero.getHeroNo(), hero.getImage()));

        savedFile.delete();
    }
}
