package com.programming.apiapplication.service;

import com.programming.apiapplication.entity.Contact;
import com.programming.apiapplication.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.programming.apiapplication.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Page<Contact> getAllContacts(int page, int size){
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContact(String id){
        return contactRepository.findById(id).orElseThrow(()-> new RuntimeException("Contact is not found"));
    }

    public  Contact createContact(Contact contact){
        return contactRepository.save(contact);
    }

    public void deleteContact(String id){
        if (getContact(id) != null) {
            contactRepository.deleteById(id);
        }
    }
    public String uploadPhoto(String id, MultipartFile file){
        log.info("Saving picture for user IF: {}", id);
        Contact contact =  getContact(id);
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;

    }

    private final Function<String, String> fileExtension = (fileName) ->{
         return Optional.of(fileName).filter(name -> name.contains("."))
                .map(name -> "." + name.substring(fileName.lastIndexOf(".") + 1))
                .orElse(".png");
    };

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image)->{
        String fileName = id + fileExtension.apply(image.getOriginalFilename());
        try{
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }
            // file will be uuid.png
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(fileName), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + fileName).toUriString();
        }catch (Exception e){
            throw new RuntimeException("unable to save the image");
        }

    };


}
