package booksystem.controller;

import booksystem.service.CollectService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class CollectController {
    @Autowired
    CollectService collectionService;

    //将书籍加入收藏夹
    @PostMapping("/collection/add")
    public Result addCollection(@RequestParam("reference_num") String reference_num,
                                ServletRequest request)
    {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=collectionService.addCollection(reference_num,username);
        if (result==1)
            return Result.ok();
        else if(result==0)
            return Result.error(ResultEnum.REPEAT_ADD.getCode(),ResultEnum.REPEAT_ADD.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    @RequestMapping("/collection/getByUser")
    public Result getCollectionByUser(ServletRequest request)
    {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        return Result.ok().put("data",collectionService.getCollectionByUser(username))
                .put("num",collectionService.getCollectionNum(username));
    }

    @DeleteMapping("/collection/delete")
    public Result deleteBook(@RequestParam("collection_id") String collection_id)
    {
        collectionService.deleteBook(collection_id);
        return Result.ok();
    }

    @DeleteMapping("/collection/multiDelete")
    public Result deleteBooks(@RequestParam("collection_ids") List<String> collection_ids)
    {
        collectionService.deleteBooks(collection_ids);
        return Result.ok();
    }
}
