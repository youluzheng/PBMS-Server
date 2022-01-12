package org.pbms.pbmsserver.controller;

import org.pbms.pbmsserver.common.auth.Role;
import org.pbms.pbmsserver.common.auth.RoleEnum;
import org.pbms.pbmsserver.common.request.tempToken.TempTokenAddDTO;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.pbms.pbmsserver.service.TempTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 临时token接口
 *
 * @author zqs
 */
@RestController
@RequestMapping("tempToken")
@Role(role = RoleEnum.ALL_LOGGED_IN)
public class TempTokenController {
    @Autowired
    private TempTokenService tempTokenService;

    @PostMapping
    public TempTokenInfo addTempToken(@Validated @RequestBody TempTokenAddDTO req) {
        return tempTokenService.addTempToken(req.getExpireTime(), req.getUploadTimes(), req.getNote());
    }

    @GetMapping
    public List<TempTokenInfo> list() {
        return tempTokenService.myTokenList();
    }

    @DeleteMapping("{tokenId}")
    public void deleteTempToken(@PathVariable long tokenId) {
        tempTokenService.deleteTempToken(tokenId);
    }
}
