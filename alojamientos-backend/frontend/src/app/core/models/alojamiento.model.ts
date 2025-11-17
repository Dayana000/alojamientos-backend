export interface Alojamiento {
  id: number;
  anfitrionId: number;
  titulo: string;
  descripcion: string;
  ciudad: string;
  direccion: string;
  latitud: number;
  longitud: number;
  precioPorNoche: number;
  capacidadMaxima: number;
  servicios: string[];
}
