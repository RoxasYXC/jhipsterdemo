import { BaseEntity } from './../../shared';

export class Userinfo implements BaseEntity {
    constructor(
        public id?: number,
        public loginid?: string,
        public password?: string,
        public name?: string,
    ) {
    }
}
