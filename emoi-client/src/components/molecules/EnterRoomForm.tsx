import React from 'react';
import * as Yup from 'yup';
import { withFormik, FormikProps, Form, Field } from 'formik';

interface FormValues {
  name: string;
}

const InnerForm = (props: FormikProps<FormValues>) => {
  const { touched, errors, isSubmitting } = props;

  return (
    <Form>
      {touched.name && errors.name && (
        <div className="roomError">{errors.name}</div>
      )}
      <div className="joinForm">
        <Field
          type="text"
          name="name"
          className="inputForm"
          placeholder="Enter a room name"
        />
        <button type="submit" className="joinButton" disabled={isSubmitting}>
          JOIN
        </button>
      </div>
    </Form>
  );
};

interface FormProps {
  initialName?: string;
  onSubmit: (name: string) => void;
}

export const EnterRoomForm = withFormik<FormProps, FormValues>({
  mapPropsToValues: (props) => {
    return {
      name: props.initialName || 'Session A',
    };
  },

  validationSchema: Yup.object().shape({
    name: Yup.string()
      .min(4, 'Room name is too short')
      .required('Room Name is Required'),
  }),

  handleSubmit: (values, { props }) => {
    const name = values.name.replace(/ /g, '-');
    props.onSubmit(name);
  },
})(InnerForm);
