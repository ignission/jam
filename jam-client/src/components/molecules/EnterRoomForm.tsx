import React from 'react';
import * as Yup from 'yup';
import { withFormik, FormikProps, Form, Field } from 'formik';
import styled from '@emotion/styled';

interface FormValues {
  name: string;
}

const RoomError = styled.div({
  fontSize: 16,
  color: '#c01515',
});

const JoinForm = styled.div({
  minWidth: 300,
  margin: '10px auto',
  maxWidth: '50%',
  background: '#fff',
  padding: '6px 10px',
  position: 'relative',
  borderRadius: '10px 5px 10px 0px',
  textAlign: 'left',
  '@media only screen and (max-width: 600px)': {
    maxWidth: '80%',
  },
});

const InputForm = styled(Field)({
  border: 0,
  padding: '4px 8px',
  fontSize: 17,
  width: 'calc(100% - 90px)',
  outline: 'none',
  caretColor: '#000',
  color: '#303030',
  '@media only screen and (min-width: 992px)': {
    fontSize: '20px !important',
  },
  '@media only screen and (max-width: 600px)': {
    fontSize: 16,
  },
});

const Button = styled.button({
  position: 'absolute',
  top: 0,
  right: -2,
  bottom: 0,
  border: 0,
  fontSize: 16,
  fontWeight: 'bold',
  padding: '0 20px',
  background: '#ec9b5f',
  color: '#fff',
  borderRadius: '0px 5px 10px 0px',
  transition: '0.3s',
  boxShadow: '0px 2px 15px rgba(0, 0, 0, 0.1)',
  margin: 0,
  cursor: 'pointer',
  '&:hover': {
    background: '#e48844',
  },
});

const InnerForm = (props: FormikProps<FormValues>) => {
  const { touched, errors, isSubmitting } = props;

  return (
    <Form>
      {touched.name && errors.name && <RoomError>{errors.name}</RoomError>}
      <JoinForm>
        <InputForm type="text" name="name" placeholder="Enter your name" />
        <Button type="submit" disabled={isSubmitting}>
          JOIN
        </Button>
      </JoinForm>
    </Form>
  );
};

interface FormProps {
  initialName: string;
  onSubmit: (name: string) => void;
}

export const EnterRoomForm = withFormik<FormProps, FormValues>({
  mapPropsToValues: (props) => {
    return {
      name: props.initialName,
    };
  },

  validationSchema: Yup.object().shape({
    name: Yup.string()
      .min(4, 'Your name is too short')
      .required('Your name is Required'),
  }),

  handleSubmit: (values, { props }) => {
    const name = values.name.replace(/ /g, '-');
    props.onSubmit(name);
  },
})(InnerForm);
