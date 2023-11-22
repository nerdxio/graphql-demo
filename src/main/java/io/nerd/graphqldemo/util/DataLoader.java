package io.nerd.graphqldemo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nerd.graphqldemo.post.PostRepository;
import io.nerd.graphqldemo.post.Posts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.InputStream;


@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger log= LoggerFactory.getLogger(DataLoader.class);
    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    public DataLoader(ObjectMapper objectMapper, PostRepository postRepository) {
        this.objectMapper = objectMapper;
        this.postRepository= postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(postRepository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/posts.json")) {
                Posts posts = objectMapper.readValue(inputStream, Posts.class);
                log.info("Reading {} posts from JSON data and saving to database.", posts.posts().size());
                postRepository.saveAll(posts.posts());

            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }
}
