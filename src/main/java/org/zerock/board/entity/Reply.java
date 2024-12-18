package org.zerock.board.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    
    private String text;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    
    private String replyer;
    
    public void changeText(String text) {
        this.text = text;
    }
} 