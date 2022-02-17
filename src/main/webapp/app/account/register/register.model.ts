export class Registration {
  constructor(
    public login: string,
    public email: string,
    public documentType: string,
    public documentNumber: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public langKey: string
  ) {}
}
