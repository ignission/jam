import React from 'react';
import { Container } from 'components/atoms/Container';
import { EnterRoomForm } from 'components/molecules/EnterRoomForm';
import styled from '@emotion/styled';

const version = require('../../../package.json').version;

interface Props {
  onSubmit: (name: string) => void;
}

const Section = styled.div({
  background: "url('images/bg.jpg')  top center no-repeat",
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
  backgroundColor: 'transparent',
  color: '#fff',
});

const HeaderImg = styled.img({
  maxWidth: 200,
  marginRight: 10,
  marginTop: 10,
});

const Logo = styled.img({
  margin: 'auto',
  '@media only screen and (max-width: 600px) ': {
    maxWidth: '80%',
  },
  '@media only screen and (min-width: 600px)': {
    maxWidth: '75%',
  },
  '@media only screen and (min-width: 992px)': {
    maxWidth: '60%',
  },
  '@media only screen and (min-width: 1200px)': {
    maxWidth: '50%',
  },
});

const Version = styled.div({
  position: 'absolute',
  right: 5,
  fontSize: 16,
  fontWeight: 'bold',
  '@media only screen and (max-width: 600px)': {
    display: 'none',
  },
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

const Home: React.FC<Props> = ({ onSubmit }) => {
  return (
    <Section>
      <Header>
        <a href="https://openvidu.io/" target="_blank">
          <HeaderImg alt="OpenVidu Logo" src="images/openvidu_logo.png" />
        </a>
        <Version>
          <span>{version}</span>
        </Version>
      </Header>
      <Container>
        <div>
          <Logo
            alt="OpenVidu Logo"
            src="images/openvidu_vert_white_bg_trans_cropped.png"
          />
          <H4>Videoconference rooms in one click</H4>
        </div>
        <FormContainer>
          <EnterRoomForm onSubmit={onSubmit} />
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

export default Home;
