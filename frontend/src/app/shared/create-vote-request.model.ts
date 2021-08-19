export interface CreateVoteModel {
  type: VoteTypeEnum;
  postName: string;
}

export enum VoteTypeEnum {
  UP_VOTE = 'UP_VOTE',
  DOWN_VOTE = 'DOWN_VOTE',
}
