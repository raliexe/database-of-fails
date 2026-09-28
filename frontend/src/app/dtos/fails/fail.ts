import { Image } from '../image/image';

export class Fail {
  id: number;
  name: string;
  description: string;
  date: string;
  mainImage: Image;
  images: Image[];
  likes: number;
  comments: number;
  likedByUser: boolean;

  constructor(id: number, name: string, description: string, date: string, mainImage: Image, images: Image[],
              likes: number, comments: number, likedByUser: boolean) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.mainImage = mainImage;
    this.images = images;
    this.likes = likes;
    this.comments = comments;
    this.likedByUser = likedByUser;
  }
}
