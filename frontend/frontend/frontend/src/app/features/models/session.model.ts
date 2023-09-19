export interface IUpdateSessionDto {
  sessionName: string;
  customerName: string;
  remark: string;
  //createdBy: string;
  sessionId:string
}

export interface ICreateSessionDto{
  sessionName:string;
  customerId:string;
  createdBy:string;
  remark:string
}

export interface ISession {
  sessionName: string;
  sessionId: string;
  remark: string;
  createdBy: string;
  createdOn: Date;
  updatedOn: Date;
  status: string;
  customerName: string;
  archiveFlag: string;
}

export interface IUserData {
  username: string;
  password: string;
}

export interface IApiResponses {
  content: ISession[];
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageNumber: number;
    pageSize: number;
    unpaged: boolean;
    paged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}
