export class CustomFile extends Blob implements File {
  name: string;
  lastModified: number = Date.now();
  webkitRelativePath: string = '';

  constructor(fileName: string, content: string, options?: BlobPropertyBag) {
    super([content], options);
    this.name = fileName;
  }
}
