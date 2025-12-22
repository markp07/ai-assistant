import type { Metadata, Viewport } from "next";
import "./globals.css";
import { ThemeProvider } from "@/components/ThemeProvider";
import Script from "next/script";

export const metadata: Metadata = {
  title: "AI Assistant",
  description: "Chat with AI Assistant",
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
  maximumScale: 1,
  userScalable: false,
  viewportFit: 'cover', // For iPhone X notch and beyond
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <head>
        <Script id="viewport-height-fix" strategy="beforeInteractive">
          {`
            // Fix for iOS Safari viewport height
            function setVH() {
              const vh = window.innerHeight * 0.01;
              document.documentElement.style.setProperty('--vh', vh + 'px');
            }
            setVH();
            window.addEventListener('resize', setVH);
            window.addEventListener('orientationchange', setVH);
          `}
        </Script>
      </head>
      <body className="antialiased">
        <ThemeProvider>{children}</ThemeProvider>
      </body>
    </html>
  );
}


