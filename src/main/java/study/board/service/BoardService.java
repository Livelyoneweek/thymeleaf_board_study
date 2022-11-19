package study.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import study.board.dto.BoardDto;
import study.board.entity.Board;
import study.board.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    //글 작성
    public void write(BoardDto boardDto) {
        Board board = new Board(boardDto.getTitle(), boardDto.getContent());
        boardRepository.save(board);
        log.info("board={}", board);
    }

    //게시글 리스트 처리
    @Transactional(readOnly = true)
    public List<BoardDto> boardList(Pageable pageable, Model model) {

        Page<Board> list = boardRepository.findAll(pageable);

        int nowPage = list.getNumber()+1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        ArrayList<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : list) {
            boardDtos.add(new BoardDto(board.getId(), board.getTitle(), board.getContent()));
        }
        return boardDtos;
    }

    //게시글 상세 내용
    @Transactional(readOnly = true)
    public BoardDto boardView(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException()
        );

        BoardDto boardDto = new BoardDto(board.getId(), board.getTitle(), board.getContent());
        return boardDto;
    }

    //게시글 삭제
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    //게시글 수정
    public void boardEdit(Long id, BoardDto boardDto) {
        Board board =boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException()
        );
        board.update(boardDto.getTitle(),boardDto.getContent());
    }

    //검색
    public List<BoardDto> boardSearchList(String searchKeyword, Pageable pageable,Model model) {

        Page<Board> list = boardRepository.findByTitleContaining(searchKeyword, pageable);

        int nowPage = list.getNumber()+1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        ArrayList<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : list) {
            boardDtos.add(new BoardDto(board.getId(), board.getTitle(), board.getContent()));
        }
        return boardDtos;

    }

    //테스터 데이터 삽입
    public void testDataInit() {
        for (int i = 0; i < 150; i++) {
            Board board = new Board("제목" + i, "타이틀" + i);
            boardRepository.save(board);
        }
        log.info("testData 삽입 완료");
    }
}
