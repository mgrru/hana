export interface FormOption {
  list: FormOptionList[];
  labelWidth?: number | string;
  span?: number;
}

export interface FormOptionList {
  prop: any;
  label: string;
  type: string;
  placeholder?: string;
  disabled?: boolean;
  opts?: any[];
  format?: string;
  activeValue?: any;
  inactiveValue?: any;
  activeText?: string;
  inactiveText?: string;
  required?: boolean;
  style?: string;
}
