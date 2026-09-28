export class PagedResponseDto<T> {
  values: T[];
  totalElements: number;
  totalPages: number;
  pageNumber: number;
  pageSize: number;
}
