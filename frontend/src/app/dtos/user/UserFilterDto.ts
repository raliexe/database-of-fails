import { PagedRequestDto } from '../PagedRequestDto';

export class UserFilterDto extends PagedRequestDto {

  email: string;
  isLocked: boolean;
}
