import React, { useState } from 'react';
import styled from '@emotion/styled';
import TextField from '@material-ui/core/TextField';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import Button, { variantType, ButtonColor } from '../atoms/Button';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      '& .MuiTextField-root': {
        padding: theme.spacing(1),
      },
    },
  })
);

const Base = styled.div({
  background: '#af62a4',
  height: '100%',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
});

const Box = styled.div({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: '100%',
  maxWidth: 600,
  padding: 80,
});

const Contents = styled.div({
  textAlign: 'center',
  maxWidth: 400,
});

const Head = styled.h1({
  color: '#fff',
});

const ButtonField = styled.div({
  display: 'flex',
  justifyContent: 'center',
  marginTop: 16,
});

const View: React.FC = () => {
  const classes = useStyles();
  return (
    <Base>
      <Box>
        <Contents>
          <Head>Sign in</Head>
          <form className={classes.root} noValidate autoComplete="off">
            <TextField label="ID" type="text" variant="filled" fullWidth />
            <TextField
              label="Password"
              type="password"
              autoComplete="current-password"
              variant="filled"
              fullWidth
            />
            <ButtonField>
              <Button
                variant={variantType.contained}
                color={ButtonColor.primary}
              >
                Sign in
              </Button>
            </ButtonField>
          </form>
        </Contents>
      </Box>
    </Base>
  );
};

export default View;
