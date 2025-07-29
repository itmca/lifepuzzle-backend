package resizer

import (
	"fmt"
	"image"
	"path/filepath"
	"strings"

	"github.com/nfnt/resize"
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
	return fmt.Sprintf("%s_%dx%d%s", nameWithoutExt, size, size, ext)
}