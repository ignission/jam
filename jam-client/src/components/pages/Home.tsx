import React from 'react';
import { EnterRoomForm } from 'components/molecules/EnterRoomForm';
import styled from '@emotion/styled';
import Logo from '../atoms/Logo';

const version = require('../../../package.json').version;

interface Props {
  initialName: string;
  onSubmit: (name: string) => void;
}

const Section = styled.div({
  background: "url('images/bg.png')  top center no-repeat",
  backgroundSize: 'cover',
  height: '100%',
  textAlign: 'center',
  position: 'relative',
  color: '#fff',
  '@media only screen and (min-width: 1200px)': {
    backgroundAttachment: 'fixed',
  },
});

const Header = styled.header({
  display: 'flex',
  backgroundColor: 'transparent',
  color: '#fff',
  alignItems: 'center',
});

const HeaderImg = styled.div({
  marginTop: 20,
  marginLeft: 20,
  color: '#fff',
  '@media only screen and (max-width: 600px)': {
    opacity: 0,
  },
});

const Version = styled.div({
  marginLeft: 'auto',
  marginRight: 10,
  fontSize: 16,
  fontWeight: 'bold',
});

const Container = styled.div({
  width: '100%',
  position: 'absolute',
  top: '40%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
});

const H4 = styled.h4({
  fontSize: 25,
  fontWeight: 500,
  color: '#fff',
  position: 'relative',
  paddingBottom: 5,
  '@media only screen and (max-width: 600px)': {
    fontSize: 16,
  },
});

const FormContainer = styled.div({
  fontSize: 20,
  textAlign: 'center',
});

const Footer = styled.footer({
  backgroundColor: 'transparent',
  color: '#fff',
  position: 'absolute',
  bottom: 0,
  fontSize: 9,
  height: 'auto',
  '@media only screen and (min-width: 1200px)': {
    display: 'none',
  },
});

const FooterLabel = styled.div({
  display: 'inline-block',
  padding: 8,
});

const FooterLink = styled.a({
  color: '#fff',
});

export const Home: React.FC<Props> = ({ initialName, onSubmit }) => {
  return (
    <Section>
      <Header>
        <a href="/">
          <HeaderImg>
            <Logo width={60} />
          </HeaderImg>
        </a>
        <Version>
          <span>{version}</span>
        </Version>
      </Header>
      <Container>
        <div>
          <Logo width={200} />
          <H4>Videoconference rooms in one click</H4>
        </div>
        <FormContainer>
          <EnterRoomForm initialName={initialName} onSubmit={onSubmit} />
        </FormContainer>
      </Container>

      <Footer>
        <FooterLabel>
          Photo by
          <FooterLink
            rel="noopener noreferrer"
            target="_blank"
            href="https://unsplash.com/@danielleone?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText"
          >
            Daniel Leone
          </FooterLink>
          on
          <FooterLink
            href="https://unsplash.com/s/photos/mountain?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText"
            target="_blank"
          >
            Unsplash
          </FooterLink>
        </FooterLabel>
      </Footer>
    </Section>
  );
};
