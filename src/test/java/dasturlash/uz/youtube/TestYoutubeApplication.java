package dasturlash.uz.youtube;

import org.springframework.boot.SpringApplication;

public class TestYoutubeApplication {

	public static void main(String[] args) {
		SpringApplication.from(YoutubeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
