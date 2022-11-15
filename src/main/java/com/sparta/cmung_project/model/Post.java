package com.sparta.cmung_project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cmung_project.dto.PostRequestDto;
import com.sparta.cmung_project.dto.PostResponseDto;
import com.sparta.cmung_project.webSocket.domain.Room;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Setter // set 함수를 일괄적으로 만들어줍니다.
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String state;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Image> image;

    public Post(PostRequestDto postRequestDto, Category category, Member member) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.price = postRequestDto.getPrice();
        this.category = category;
        this.member = member;
        this.state = postRequestDto.getState();
    }

    public void update (PostRequestDto postRequestDto, Category category) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = category;
        this.state = postRequestDto.getState();
        this.price = postRequestDto.getPrice();
    }

    public PostResponseDto toDto() {
        // 이미지 객체 리스트를 문자열 리스트로 변환
        List<String> imageList = this.image
                .stream().map((imageObj) -> {
                    // 포스트 객체를 DTO로 만든다.
                    String imageStr = imageObj.getImage();

                    // DTO 반환
                    return imageStr;
                })
                .collect(Collectors.toList());

        // DTO 반환
        return new PostResponseDto(this.id, this.title, this.content, this.price, this.category.getName(), imageList);
    }
}
