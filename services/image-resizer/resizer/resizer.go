package resizer

import (
	"bytes"
	"fmt"
	"image"
	"image/jpeg"
	"image/png"
	"path/filepath"
	"strings"

	"github.com/adrium/goheif"
	"github.com/chai2010/webp"
	"github.com/nfnt/resize"
	xwebp "golang.org/x/image/webp"
)

type ImageResizer struct {
	supportedSizes []int
}

func NewImageResizer() *ImageResizer {
	return &ImageResizer{
		supportedSizes: []int{1280, 640, 240},
	}
}

func (r *ImageResizer) GetMissingSizes(existingSizes []int) []int {
	var missing []int
	
	existingMap := make(map[int]bool)
	for _, size := range existingSizes {
		existingMap[size] = true
	}
	
	for _, size := range r.supportedSizes {
		if !existingMap[size] {
			missing = append(missing, size)
		}
	}
	
	return missing
}

func (r *ImageResizer) ResizeImage(img image.Image, targetSize int) image.Image {
	bounds := img.Bounds()
	width := bounds.Dx()
	height := bounds.Dy()
	
	// Limit maximum size to 1280px
	maxSize := 1280
	if targetSize > maxSize {
		targetSize = maxSize
	}
	
	// If image is already smaller than target size, don't upscale
	if width <= targetSize && height <= targetSize {
		return img
	}
	
	var newWidth, newHeight uint
	
	if width > height {
		newWidth = uint(targetSize)
		newHeight = uint(float64(height) * float64(targetSize) / float64(width))
	} else {
		newHeight = uint(targetSize)
		newWidth = uint(float64(width) * float64(targetSize) / float64(height))
	}
	
	return resize.Resize(newWidth, newHeight, img, resize.Lanczos3)
}

func (r *ImageResizer) GenerateResizedPath(originalPath string, size int) string {
	ext := filepath.Ext(originalPath)
	nameWithoutExt := strings.TrimSuffix(originalPath, ext)
	// Always use .webp extension for resized images
	return fmt.Sprintf("%s_%dx%d.webp", nameWithoutExt, size, size)
}

// ConvertToWebP converts an image to WebP format and returns the bytes
func (r *ImageResizer) ConvertToWebP(img image.Image) ([]byte, error) {
	buf := new(bytes.Buffer)
	if err := webp.Encode(buf, img, &webp.Options{Lossless: false, Quality: 80}); err != nil {
		return nil, fmt.Errorf("failed to encode image as WebP: %w", err)
	}
	return buf.Bytes(), nil
}

// DecodeImage decodes an image from bytes, supporting JPEG, PNG, WebP, and HEIC
func (r *ImageResizer) DecodeImage(data []byte) (image.Image, error) {
	reader := bytes.NewReader(data)
	
	// Try to decode as WebP first
	if img, err := xwebp.Decode(reader); err == nil {
		return img, nil
	}
	
	// Reset reader
	reader.Seek(0, 0)
	
	// Try to decode as JPEG
	if img, err := jpeg.Decode(reader); err == nil {
		return img, nil
	}
	
	// Reset reader
	reader.Seek(0, 0)
	
	// Try to decode as PNG
	if img, err := png.Decode(reader); err == nil {
		return img, nil
	}
	
	// Try to decode as HEIC/HEIF last
	if img, err := r.decodeHEIC(data); err == nil {
		return img, nil
	}
	
	return nil, fmt.Errorf("unsupported image format")
}

// decodeHEIC decodes HEIC/HEIF image format
func (r *ImageResizer) decodeHEIC(data []byte) (image.Image, error) {
	// Check if the data starts with HEIC/HEIF magic bytes
	if len(data) < 12 {
		return nil, fmt.Errorf("data too short for HEIC")
	}
	
	// HEIC files start with specific byte patterns
	// ftyp box at offset 4, followed by brand (heic, heix, heis, etc.)
	if !bytes.Equal(data[4:8], []byte("ftyp")) {
		return nil, fmt.Errorf("not a valid HEIC file")
	}
	
	// Check for HEIC brand signatures
	brand := data[8:12]
	if !bytes.Equal(brand, []byte("heic")) && 
	   !bytes.Equal(brand, []byte("heix")) && 
	   !bytes.Equal(brand, []byte("heis")) &&
	   !bytes.Equal(brand, []byte("hevm")) &&
	   !bytes.Equal(brand, []byte("heim")) {
		return nil, fmt.Errorf("not a supported HEIC brand")
	}
	
	// Use goheif to decode HEIC image
	reader := bytes.NewReader(data)
	img, err := goheif.Decode(reader)
	if err != nil {
		return nil, fmt.Errorf("failed to decode HEIF image: %w", err)
	}
	
	return img, nil
}

// ProcessImage converts image to WebP and resizes if necessary
func (r *ImageResizer) ProcessImage(data []byte) ([]byte, error) {
	img, err := r.DecodeImage(data)
	if err != nil {
		return nil, err
	}
	
	// Check if image needs resizing (larger than 1280px)
	bounds := img.Bounds()
	width := bounds.Dx()
	height := bounds.Dy()
	
	if width > 1280 || height > 1280 {
		img = r.ResizeImage(img, 1280)
	}
	
	// Convert to WebP
	return r.ConvertToWebP(img)
}