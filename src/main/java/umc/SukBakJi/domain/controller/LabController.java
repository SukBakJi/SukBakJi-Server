package umc.SukBakJi.domain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.LabDetailResponseDTO;
import umc.SukBakJi.domain.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.model.dto.SearchLabRequestDTO;
import umc.SukBakJi.domain.service.LabService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
public class LabController {

    private final LabService labService;

    @Autowired
    public LabController(LabService labService) {
        this.labService = labService;
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<LabResponseDTO>>> searchLabs(@RequestBody SearchLabRequestDTO searchRequest) {
        List<LabResponseDTO> labs = labService.searchLabsByTopicName(searchRequest.getTopicName());
        return ResponseEntity.ok(ApiResponse.onSuccess(labs));
    }

    @GetMapping("/{labId}")
    public ResponseEntity<ApiResponse<LabDetailResponseDTO>> getLabDetail(@PathVariable Long labId) {
        try {
            LabDetailResponseDTO labDetail = labService.getLabDetail(labId);
            return ResponseEntity.ok(ApiResponse.onSuccess(labDetail));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        }
    }
}
