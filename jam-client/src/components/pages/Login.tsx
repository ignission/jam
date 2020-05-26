import React, { useState } from 'react';
import styled from '@emotion/styled';
import TextField from '@material-ui/core/TextField';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      '& .MuiTextField-root': {
        margin: theme.spacing(1),
        // width: 200,
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
  // height: '100%',
  maxWidth: 600,
  // background: '#fff',
  padding: 80,
});

const Contents = styled.div({
  // background: '#fff',
  maxWidth: 400,
  // height: '100%',
});

const View: React.FC = () => {
  const classes = useStyles();
  return (
    <Base>
      <Box>
        <Contents>
          <form className={classes.root} noValidate autoComplete="off">
            <TextField label="ID" type="text" variant="filled" fullWidth />
            <TextField
              label="Password"
              type="password"
              autoComplete="current-password"
              variant="filled"
              fullWidth
            />
          </form>
        </Contents>
      </Box>
    </Base>
  );
};

export default View;
