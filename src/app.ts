import express from 'express';
import cors from 'cors';
import morgan from 'morgan';
import { errorHandler } from './middlewares/errorHandler';
import routes from './routes';

const app = express();

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(morgan('dev'));


app.get('/test-videos', (req, res) => {
    res.json({ message: 'test route works' });
});
app.use('/api', routes);

app.use(errorHandler);

export default app;