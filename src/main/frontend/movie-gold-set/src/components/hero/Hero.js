/* import './Hero.css';
import Carousel from 'react-material-ui-carousel';
import { Paper } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCirclePlay } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from 'react-bootstrap';


const Hero = ({movies}) => {

  const navigate = useNavigate();

  function reviews(movieId) {
    navigate(`/Reviews/${movieId}`);
  }

  return (
    <div className='movie-carousel-container'>
      <Carousel>
        {
          movies?.map((movie) =>{
            return(
              <Paper key={movie.imdbId}>
                <div className='movie-card-container'>
                  <div className='movie-card' style={{"--img": `url(${movie.backdrops[0]})`}}>
                    <div className='movie-detail'>
                      <div className='movie-poster'>
                        <img src={movie.poster} alt='' />
                      </div>
                      <div className='movie-title'>
                        <h2>{movie.title}</h2>
                      </div>
                      <div className='movie-buttons-container'>
                        <Link to={`/Trailer/${movie.trailerLink.substring(movie.trailerLink.length - 11)}`}>
                          <div className='play-button-icon-container'>
                            <FontAwesomeIcon className='play-button-icon'
                              icon={faCirclePlay}
                            />
                          </div>
                        </Link>
                        <div className='movie-review-button-container'>
                          <Button variant='outline-light' onClick={() => reviews(movie.imdbId)}>Reviews</Button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </Paper>
            )
          })
        }
      </Carousel>
    </div>
  )
}

export default Hero */

import './Hero.css';
import Carousel from 'react-material-ui-carousel';
import { Paper } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCirclePlay } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from 'react-bootstrap';
import AuthService from '../../services/auth.service'; // Імпорт сервісу авторизації

const Hero = ({ movies }) => {
  const navigate = useNavigate();
  const isAuthenticated = AuthService.getCurrentUser(); // Перевірка авторизації

  function reviews(movieId) {
    navigate(`/Reviews/${movieId}`);
  }

  return (
    <div className='movie-carousel-container'>
      <Carousel>
        {movies?.map((movie) => {
          return (
            <Paper key={movie.imdbId}>
              <div className='movie-card-container'>
                <div className='movie-card' style={{ "--img": `url(${movie.backdrops[0]})` }}>
                  <div className='movie-detail'>
                    <div className='movie-poster'>
                      <img src={movie.poster} alt='' />
                    </div>
                    <div className='movie-title'>
                      <h2>{movie.title}</h2>
                    </div>
                    <div className='movie-buttons-container'>
                      <Link to={`/Trailer/${movie.trailerLink.substring(movie.trailerLink.length - 11)}`}>
                        <div className='play-button-icon-container'>
                          <FontAwesomeIcon className='play-button-icon' icon={faCirclePlay} />
                        </div>
                        <span className='full-movie-link'>Trailer</span>
                      </Link>
                      {isAuthenticated && (
                        <Link to={`/Trailer/${movie.fullMovieLink.substring(movie.fullMovieLink.length - 11)}`}>
                          <div className='play-button-icon-container2'>
                            <FontAwesomeIcon className='play-button-icon2' icon={faCirclePlay} />
                          </div>
                          <span className='full-movie-link'>Full Movie</span>
                        </Link>
                      )}
                      <div className='movie-review-button-container'>
                        <Button variant='outline-light' onClick={() => reviews(movie.imdbId)}>Reviews</Button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </Paper>
          )
        })}
      </Carousel>
    </div>
  )
}

export default Hero;
