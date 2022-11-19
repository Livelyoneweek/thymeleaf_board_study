package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.board.dto.BoardDto;
import study.board.entity.Board;
import study.board.service.BoardService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    @ResponseBody
    public String boardWritePro(BoardDto boardDto) {
        boardService.write(boardDto);
        return "ok";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page=0,size=10,sort="id",direction=Sort.Direction.ASC)Pageable pageable,
                            @RequestParam(value = "searchKeyword" , required = false) String searchKeyword) {

        List<BoardDto> boardDtos = null;
        if (searchKeyword == null) {
            boardDtos = boardService.boardList(pageable, model);
        } else {
            boardDtos = boardService.boardSearchList(searchKeyword,pageable, model);
        }

        model.addAttribute("list", boardDtos);
        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView(@RequestParam Long id, Model model) {
        BoardDto boardDto = boardService.boardView(id);
        model.addAttribute("border", boardDto);
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam Long id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    // 수정 뷰 가기
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model) {

        BoardDto boardDto = boardService.boardView(id);
        model.addAttribute("board", boardDto);
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, BoardDto boardDto) {
        log.info(boardDto.toString());
        boardService.boardEdit(id, boardDto);
        return "redirect:/board/list";
    }

    //테스트 데이터 삽입 컨트롤러, 한번만 실행할 것
    @GetMapping("/test")
    @ResponseBody
    public String testDataInit() {
        boardService.testDataInit();
        return "테스트데이터삽입완료";
    }


}
