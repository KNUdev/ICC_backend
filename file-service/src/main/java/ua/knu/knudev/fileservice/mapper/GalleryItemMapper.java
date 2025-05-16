package ua.knu.knudev.fileservice.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.fileservice.domain.GalleryItem;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface GalleryItemMapper extends BaseMapper<GalleryItem, GalleryItemDto> {
}