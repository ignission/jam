import React from 'react';
import * as Yup from 'yup';
import { withFormik, FormikProps, FormikErrors, Form, Field } from 'formik';

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
        <Field type="text" name="name" className="inputForm" />
        <button type="submit" className="joinButton" disabled={isSubmitting}>
          JOIN
        </button>
      </div>
    </Form>
  );
};

interface FormProps {
  initialName?: string;
}

export const EnterRoomForm = withFormik<FormProps, FormValues>({
  mapPropsToValues: (props) => {
    return {
      name: props.initialName || 'Session A',
    };
  },

  validate: (values: FormValues) => {
    let errors: FormikErrors<FormValues> = {};
    if (!values.name) {
      errors.name = 'Room Name is Required';
    } else if (values.name.length < 5) {
      errors.name = 'Room name is too short';
    }
    return errors;
  },

  handleSubmit: (values) => {
    const name = values.name.replace(/ /g, '-');

    // do submitting things
  },
})(InnerForm);
