package com.example.controller;

import com.example.dto.BulkAssignDTO;
import com.example.dto.GroupProfileDTO;
import com.example.dto.group.GroupCreateDTO;
import com.example.dto.group.GroupDTO;
import com.example.dto.group.GroupUpdateDTO;
import com.example.dto.profile.ProfileInfoDTO;
import com.example.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/group")
@Tag(name = "Group APIs", description = "API for groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/admin")
    @Operation(summary = "Create group", description = "Api used for create group by admin")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupCreateDTO group) {
        return ResponseEntity.ok(groupService.create(group));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/admin/{id}")
    @Operation(summary = "Update group", description = "Api used for update group by admin")
    public ResponseEntity<String> updateGroup(@PathVariable("id") String id, @RequestBody GroupUpdateDTO group) {
        return ResponseEntity.ok(groupService.update(id,group));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/admin/{id}")
    @Operation(summary = "Delete group", description = "Api used for delete group by admin")
    public ResponseEntity<String> deleteGroup(@PathVariable("id") String id) {
        return ResponseEntity.ok(groupService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-by-id/admin/{id}")
    @Operation(summary = "Get By id", description = "Api used for get group by id")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable("id") String id) {
        return ResponseEntity.ok(groupService.getGroup(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assign-student")
    public ResponseEntity<String> assignStudent(
            @RequestParam("studentId") String studentId,
            @RequestParam("groupId") String groupId) {
        return ResponseEntity.ok(groupService.assignStudentToGroup(studentId, groupId));
    }

    // 6.7 Bulk Assign
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bulk-assign-students")
    public ResponseEntity<String> bulkAssign(@RequestBody BulkAssignDTO dto) {
        return ResponseEntity.ok(groupService.bulkAssign(dto.getStudentIds(), dto.getGroupId()));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-list/admin/{schoolId}")
    @Operation(summary = "Get by school Id", description = "Api used for get group by school id")
    public ResponseEntity<Page<GroupDTO>> getGroupBySchoolId(
            @PathVariable("schoolId") String schoolId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(groupService.getAllBySchoolId(schoolId, page, size));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/{groupId}")
    public ResponseEntity<List<GroupProfileDTO>> getStudents(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getStudentsByGroup(groupId));
    }
}
